# faq_loader.py
import csv

def load_faq(path):
    faq = []
    with open(path, 'r', encoding='utf-8') as f:
        reader = csv.reader(f)
        next(reader)  # skip header
        for row in reader:
            if len(row) == 2:
                faq.append((row[0], row[1]))  # (question, answer)
    return faq
