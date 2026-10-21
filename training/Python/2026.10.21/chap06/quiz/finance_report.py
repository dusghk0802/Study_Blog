from openai import OpenAI
from dotenv import load_dotenv
import os
import base64

load_dotenv()
api_key = os.getenv("OPENAI_API_KEY")
client = OpenAI(api_key=api_key) 


def encode_image(image_path):
    with open(image_path, "rb") as image_file:
        return base64.b64encode(image_file.read()).decode("utf-8")


def analyze_image(image_path):
    base64_image = encode_image(image_path)

    ext = os.path.splitext(image_path)[1].lower()
    mime = "image/png" if ext == ".png" else "image/jpeg"

    report_prompt = """
    제공된 금융 이미지를 분석해서 아래 양식에 맞춰 한국어로 작성해 주세요.
    이미지에서 확인할 수 없는 내용은 추측하지 말고 "이미지에서 확인 불가"라고 적어 주세요.

    ### 이미지 설명
    (어떤 종류의 금융 이미지인지, 기간과 대상, 축과 단위 등을 설명)

    ### 핵심 데이터 요약
    - (이미지에서 읽을 수 있는 주요 수치와 변화를 3~5개 항목으로 정리)

    ### 금융적 의미 분석
    (이 데이터가 투자자, 기업, 경제에 어떤 의미가 있는지 분석)
    """

    messages = [
        {
            "role": "user",
            "content": [
                {"type": "text", "text": report_prompt},
                {
                    "type": "image_url",
                    "image_url": {"url": f"data:{mime};base64,{base64_image}"},
                },
            ],
        }
    ]

    response = client.chat.completions.create(
        model="gpt-4o",  # 응답 생성에 사용할 모델 지정
        messages=messages,
    )
    return response.choices[0].message.content


image_dir = "../data/images"
image_names = [
    "주가 차트.png",
    "환율 그래프.png",
    "금리 그래프.png",
    "물가(CPI) 그래프.png",
    "기업 실적 자료.png",
    "비트코인 가격 차트.png",
]
image_list = [f"{image_dir}/{name}" for name in image_names]

txt = "# AI를 활용한 금융 이미지 분석 보고서\n\n"
no = 1  # 이미지 번호

for g in image_list:
    print(f"{no}번 이미지 분석 중: {g}")
    result = analyze_image(g)

    filename = os.path.basename(g)
    txt += f"## {no}. {filename}\n\n"
    # 보고서에 이미지 표시 (파일 이름에 공백이 있어 < >로 감쌈)
    txt += f"![{filename}](<../data/images/{filename}>)\n\n"
    txt += result + "\n\n---\n\n"
    no += 1

# 마크다운 파일로 저장
with open("./finance_report.md", "w", encoding="utf-8") as f:
    f.write(txt)

print("보고서 저장 완료: finance_report.md")
