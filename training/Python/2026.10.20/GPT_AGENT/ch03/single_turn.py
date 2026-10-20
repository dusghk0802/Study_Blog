from openai import OpenAI
from dotenv import load_dotenv
import os

load_dotenv() 
api_key = os.getenv('OPENAI_API_KEY')

client = OpenAI(api_key=api_key)

while True:
  user_input = input("사용자: ")
  
  if user_input == "exit":
    break
  
  response = client.chat.completions.create(
    model="gpt-4o",
    temperature=0.9, # 0~1
    messages=[
        {"role": "system", "content": "너는 사용자를 도와주는 상담사야."}, # gpt 역할 설정
        {"role": "user", "content": user_input} 
        # 모델과 대화를 나눈는 사용자, assistant: 언어모델이 제공하는 답변
    ]
  )
  print("AI : ", response.choices[0].message.content)