from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import logging
import pytesseract
from PIL import Image
import io
import PyPDF2
from pdf2image import convert_from_path
import google.generativeai as genai
from dotenv import load_dotenv
import tempfile
from image_enhancer import ImageEnhancer

# Load environment variables
load_dotenv()

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Set the path to Tesseract executable
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

# Configure Gemini API
GEMINI_API_KEY = os.getenv('GEMINI_API_KEY')
if not GEMINI_API_KEY:
    logger.warning("No Gemini API key found. Please set GEMINI_API_KEY in .env file")

genai.configure(api_key=GEMINI_API_KEY)
model = genai.GenerativeModel('gemini-2.0-flash-lite')

app = Flask(__name__)
CORS(app)

def extract_text_from_pdf(file):
    """Extract text from a PDF file using OCR"""
    try:
        # Create a temporary file
        with tempfile.NamedTemporaryFile(delete=False, suffix='.pdf') as temp_pdf:
            file.save(temp_pdf.name)
            temp_pdf_path = temp_pdf.name

        try:
            # First try PyPDF2 for text-based PDFs
            pdf_reader = PyPDF2.PdfReader(temp_pdf_path)
            text = ""
            has_text = False

            for page_num in range(len(pdf_reader.pages)):
                page = pdf_reader.pages[page_num]
                page_text = page.extract_text()

                if page_text.strip():
                    text += f"\n--- Page {page_num + 1} ---\n{page_text}\n"
                    has_text = True
                else:
                    # If no text found, convert page to image and use OCR
                    images = convert_from_path(temp_pdf_path, first_page=page_num+1, last_page=page_num+1)
                    if images:
                        # Enhance the image before OCR
                        enhanced_image = ImageEnhancer.enhance_pdf_page(images[0])
                        page_text = pytesseract.image_to_string(enhanced_image)
                        text += f"\n--- Page {page_num + 1} (OCR) ---\n{page_text}\n"
                        has_text = True

            if not has_text:
                # If no text found in any page, try OCR on the entire PDF
                images = convert_from_path(temp_pdf_path)
                text = ""
                for i, image in enumerate(images):
                    # Enhance each image before OCR
                    enhanced_image = ImageEnhancer.enhance_pdf_page(image)
                    page_text = pytesseract.image_to_string(enhanced_image)
                    text += f"\n--- Page {i + 1} (OCR) ---\n{page_text}\n"

            return text

        finally:
            # Clean up temporary file
            if os.path.exists(temp_pdf_path):
                os.remove(temp_pdf_path)

    except Exception as e:
        logger.error(f"Error extracting text from PDF: {str(e)}")
        if 'temp_pdf_path' in locals() and os.path.exists(temp_pdf_path):
            os.remove(temp_pdf_path)
        raise Exception(f"Failed to extract text from PDF: {str(e)}")

def extract_text_from_image(file):
    """Extract text from an image using OCR"""
    try:
        # Open the image
        image = Image.open(file)

        # Convert image to bytes for enhancement
        img_byte_arr = io.BytesIO()
        image.save(img_byte_arr, format=image.format)
        img_byte_arr = img_byte_arr.getvalue()

        # Enhance the image
        enhanced_image_data = ImageEnhancer.enhance_image(img_byte_arr)
        enhanced_image = Image.open(io.BytesIO(enhanced_image_data))

        # Perform OCR on enhanced image
        text = pytesseract.image_to_string(enhanced_image)
        return text
    except Exception as e:
        logger.error(f"Error extracting text from image: {str(e)}")
        raise Exception(f"Failed to extract text from image: {str(e)}")

def process_file(file):
    """Process either PDF or image file"""
    try:
        # Check file type
        file_extension = os.path.splitext(file.filename)[1].lower()

        if file_extension in ['.pdf']:
            return extract_text_from_pdf(file)
        elif file_extension in ['.jpg', '.jpeg', '.png', '.bmp', '.tiff']:
            return extract_text_from_image(file)
        else:
            raise ValueError(f"Unsupported file type: {file_extension}")
    except Exception as e:
        logger.error(f"Error processing file: {str(e)}")
        raise

def generate_summary(text):
    """Generate summary using Gemini"""
    try:
        prompt = f"""
        Please analyze the following document and provide:
        1. A concise summary (2-3 paragraphs)
        2. Key points (5-7 bullet points)

        Document:
        {text}
        """

        response = model.generate_content(prompt)
        return response.text
    except Exception as e:
        logger.error(f"Error generating summary: {str(e)}")
        raise Exception(f"Failed to generate summary: {str(e)}")

@app.route('/extract-text', methods=['POST'])
def extract_text():
    """Extract text from file using OCR"""
    try:
        if 'file' not in request.files:
            return jsonify({'error': 'No file provided'}), 400

        file = request.files['file']
        if file.filename == '':
            return jsonify({'error': 'No file selected'}), 400

        # Extract text from file
        text = process_file(file)
        if not text.strip():
            return jsonify({'error': 'No text could be extracted from the file'}), 400

        return jsonify({
            'text': text,
            'file_type': os.path.splitext(file.filename)[1].lower()
        })

    except Exception as e:
        logger.error(f"Error in text extraction: {str(e)}")
        return jsonify({'error': str(e)}), 500

@app.route('/summarize', methods=['POST'])
def summarize():
    """Generate summary using Gemini"""
    try:
        if 'file' not in request.files:
            return jsonify({'error': 'No file provided'}), 400

        file = request.files['file']
        if file.filename == '':
            return jsonify({'error': 'No file selected'}), 400

        # Extract text from file
        text = process_file(file)
        if not text.strip():
            return jsonify({'error': 'No text could be extracted from the file'}), 400

        # Generate summary
        summary = generate_summary(text)

        return jsonify({
            'summary': summary,
            'model': 'gemini-2.0-flash-lite'
        })

    except Exception as e:
        logger.error(f"Error in summarization: {str(e)}")
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)