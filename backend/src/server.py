from flask import Flask, request, jsonify
from flask_cors import CORS
from document_summarizer import DocumentSummarizer
import os
import tempfile

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes
summarizer = DocumentSummarizer()

@app.route('/summarize', methods=['POST'])
def summarize_document():
    if 'file' not in request.files:
        return jsonify({'error': 'No file provided'}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No file selected'}), 400

    # Create a temporary file to store the uploaded file
    with tempfile.NamedTemporaryFile(delete=False, suffix=os.path.splitext(file.filename)[1]) as temp_file:
        file.save(temp_file.name)
        try:
            summary = summarizer.process_document(temp_file.name)
            return jsonify({'summary': summary})
        except Exception as e:
            return jsonify({'error': str(e)}), 500
        finally:
            # Clean up the temporary file
            os.unlink(temp_file.name)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)