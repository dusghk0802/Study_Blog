from openai import OpenAI
from dotenv import load_dotenv
import os

load_dotenv() 
api_key = os.getenv('OPENAI_API_KEY')

client = OpenAI(api_key=api_key)

response = client.chat.completions.create(
    model="gpt-4o",
    temperature=0.1, # 0~1
    messages=[
        {"role": "system", "content": "You are a helpful assistant."}, # gpt 역할 설정
        {"role": "user", "content": "2002년 월드컵 우승팀은 어디야"} 
        # 모델과 대화를 나눈는 사용자, assistant: 언어모델이 제공하는 답변
    ]
)

print(response)
print('----------------------')
print(response.choices[0].message.content)