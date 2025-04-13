# Document Summarization Backend

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

## Setup Instructions

1. Create a virtual environment (recommended):
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

2. Install dependencies:
```bash
pip install -r requirements.txt
python -m spacy download en_core_web_sm
```

3. Run the server:
```bash
python src/server.py
```

The server will run on http://localhost:5000