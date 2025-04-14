from flask import Flask, request, render_template, jsonify
from transformers import AutoTokenizer, AutoModel
import torch
import pandas as pd
import torch.nn.functional as F

app = Flask(__name__)

# Load model and tokenizer
MODEL_NAME = "nlpaueb/legal-bert-base-uncased"
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)
model = AutoModel.from_pretrained(MODEL_NAME)

# Load and clean FAQs
faq_df = pd.read_csv("faq.csv")

# Drop rows where 'question' or 'answer' is NaN
faq_df = faq_df.dropna(subset=["question", "answer"])

# Ensure all are strings
faq_df["question"] = faq_df["question"].astype(str)
faq_df["answer"] = faq_df["answer"].astype(str)

faq_questions = faq_df['question'].tolist()
faq_answers = faq_df['answer'].tolist()

# Compute embeddings for all FAQ questions
def get_embedding(text):
    if not isinstance(text, str):
        raise ValueError(f"Expected text to be a string, but got {type(text)}")
    inputs = tokenizer(text, return_tensors='pt', truncation=True, padding=True)
    with torch.no_grad():
        outputs = model(**inputs)
        # Mean pooling of last hidden states
        embeddings = outputs.last_hidden_state.mean(dim=1)
    return embeddings[0]

# Compute embeddings safely
faq_embeddings = torch.stack([get_embedding(q) for q in faq_questions])

@app.route("/")
def home():
    return render_template("index.html")  # Ensure this file exists

@app.route("/ask", methods=["POST"])
def ask():
    user_question = request.json.get("question", "")
    if not user_question.strip():
        return jsonify({"answer": "Please enter a question."})

    try:
        user_embedding = get_embedding(user_question)
        similarities = F.cosine_similarity(user_embedding.unsqueeze(0), faq_embeddings)
        best_idx = similarities.argmax().item()
        best_answer = faq_answers[best_idx]
        return jsonify({"answer": best_answer})
    except Exception as e:
        return jsonify({"answer": f"An error occurred: {str(e)}"})

if __name__ == "__main__":
    app.run(debug=True)
