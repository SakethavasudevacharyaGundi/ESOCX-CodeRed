import spacy
from transformers import pipeline
import PyPDF2
import docx
import os

class DocumentSummarizer:
    def __init__(self):
        # Load spaCy model
        self.nlp = spacy.load("en_core_web_sm")
        # Initialize summarization pipeline
        self.summarizer = pipeline("summarization", model="facebook/bart-large-cnn")

    def read_pdf(self, file_path):
        """Read text from PDF file"""
        text = ""
        with open(file_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            for page in pdf_reader.pages:
                text += page.extract_text()
        return text

    def read_docx(self, file_path):
        """Read text from DOCX file"""
        doc = docx.Document(file_path)
        text = ""
        for paragraph in doc.paragraphs:
            text += paragraph.text + "\n"
        return text

    def read_txt(self, file_path):
        """Read text from TXT file"""
        with open(file_path, 'r', encoding='utf-8') as file:
            return file.read()

    def preprocess_text(self, text):
        """Preprocess text using spaCy"""
        doc = self.nlp(text)
        # Remove stop words and perform basic cleaning
        cleaned_text = " ".join([token.text for token in doc if not token.is_stop])
        return cleaned_text

    def summarize(self, text, max_length=150, min_length=30):
        """Generate summary using BART model"""
        # Split text into chunks if it's too long
        chunks = [text[i:i+1024] for i in range(0, len(text), 1024)]
        summaries = []

        for chunk in chunks:
            summary = self.summarizer(chunk, max_length=max_length, min_length=min_length, do_sample=False)
            summaries.append(summary[0]['summary_text'])

        return " ".join(summaries)

    def process_document(self, file_path):
        """Process document based on its extension"""
        _, ext = os.path.splitext(file_path)
        ext = ext.lower()

        if ext == '.pdf':
            text = self.read_pdf(file_path)
        elif ext == '.docx':
            text = self.read_docx(file_path)
        elif ext == '.txt':
            text = self.read_txt(file_path)
        else:
            raise ValueError(f"Unsupported file format: {ext}")

        # Preprocess and summarize
        cleaned_text = self.preprocess_text(text)
        summary = self.summarize(cleaned_text)
        return summary