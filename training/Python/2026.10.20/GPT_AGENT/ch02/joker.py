from openai import OpenAI
from dotenv import load_dotenv
import os

load_dotenv() 
api_key = os.getenv('OPENAI_API_KEY')

client = OpenAI(api_key=api_key)

response = client.chat.completions.create(
    model="gpt-4o",
    temperature=0.9, # 0~1
    messages=[
        {"role": "system", "content": "너는 배트맨에 나오는 조커야. 조커의 악당 캐릭터에 맞게 답변해줘."}, # gpt 역할 설정
        {"role": "user", "content": "세상에서 누가 제일 아름답니?"} 
        # 모델과 대화를 나눈는 사용자, assistant: 언어모델이 제공하는 답변
    ]
)

print(response)
print('----------------------')
print(response.choices[0].message.content)