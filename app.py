from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def main():
    
    # 템플릿을 렌더링할 때 모달 리스트 전체를 전달합니다.
    return render_template('index.html')

if __name__ == '__main__':
    app.run(debug=True)