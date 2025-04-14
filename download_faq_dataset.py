from datasets import load_dataset
import pandas as pd

# Load the dataset from Hugging Face
dataset = load_dataset("viber1/indian-law-dataset")

# Convert to pandas DataFrame and save as CSV
faq_data = dataset['train']
faq_df = faq_data.to_pandas()

# Rename the columns to 'question' and 'answer'
faq_df.rename(columns={"Instruction": "question", "Response": "answer"}, inplace=True)

# Save to CSV
faq_df.to_csv('faq.csv', index=False)

print("Dataset downloaded and saved as 'faq.csv'")
