# Legal Document Interpreter Backend

This directory contains the Python backend for the document summarization service.

## Directory Structure
```
backend/
├── src/
│   ├── __init__.py
│   ├── document_summarizer.py    # Core summarization logic
│   └── server.py                 # Flask server implementation
├── requirements.txt              # Python dependencies
└── README.md                     # This file
```

## Setup

1. Create a virtual environment and activate it:
```bash
python -m venv venv
.\venv\Scripts\activate  # Windows
source venv/bin/activate  # Linux/Mac
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Set up environment variables:
   - Copy `.env.example` to `.env`
   - Add your Gemini API key to `.env`
```bash
cp .env.example .env
# Edit .env and add your API key
```

4. Run the server:
```bash
python src/server.py
```

## Environment Variables

- `GEMINI_API_KEY`: Your Google Gemini API key

## API Endpoints

- `POST /extract-text`: Extract text from PDF/image files
- `POST /summarize`: Generate summary using Gemini API

## Security Notes

- Never commit your `.env` file
- Keep your API keys secure
- Use environment variables for sensitive data