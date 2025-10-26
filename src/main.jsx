import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'

// CSS 파일을 여기서 전역으로 불러옵니다.
import './style.css' 

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)