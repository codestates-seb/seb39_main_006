import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Button from "../../components/ui/Button";
import styled from "styled-components";
import H1 from "../../components/ui/H1";
import WrapperBox from "../../components/ui/WrapperBox";
// toast
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";

// toast color syntax
import "tui-color-picker/dist/tui-color-picker.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";

const NewPost = () => {
  const navigate = useNavigate();

  const bodyRef = useRef();
  const [title, setTitle] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [location, setLocation] = useState("");
  const [totalCount, setTotalCount] = useState(0);
  const [closeDate, setCloseDate] = useState("");

  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const date = today.getDate();

  const submitHandler = () => {
    const enteredBody = bodyRef.current?.getInstance().getMarkdown();

    axios(`https://seb-006.shop/api/posts`, {
      method: "POST",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
      data: {
        title: title,
        body: enteredBody,
        startDate: startDate,
        endDate: endDate,
        location: location,
        totalCount: totalCount,
        closeDate: closeDate,
        images: [],
      },
    })
      .then((res) => {
        if (res.headers.access_hh) {
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
        }
        navigate(`/auth`);
        window.location.reload();
      })
      .catch((err) => {
        if (err.response.status === 500) {
          alert("세션이 만료되어 로그아웃합니다.");
          sessionStorage.clear();
          navigate(`/`);
          window.location.reload();
        }
      });
  };

  return (
    <>
      <H1>글 작성 페이지</H1>
      <WrapperBox></WrapperBox>
      <div>
        <div className="container">
          <InputWrapper>
            <span>제목 </span>
            <input
              id="title"
              type="text"
              required
              onChange={(e) => {
                setTitle(e.target.value);
              }}
            />
          </InputWrapper>
          <InputWrapper>
            <span>여행 출발 날짜 </span>
            <input
              className="date"
              type="date"
              required
              onChange={(e) => {
                setStartDate(e.target.value);
              }}
              min={`${year}-${("0" + month).slice(-2)}-${date}`}
            />
          </InputWrapper>
          <InputWrapper>
            <span>여행 종료? 날짜 </span>
            <input
              className="date"
              type="date"
              required
              onChange={(e) => {
                setEndDate(e.target.value);
              }}
              min={startDate}
            />
          </InputWrapper>
          <InputWrapper>
            <span>모집 마감 </span>
            <input
              type="date"
              required
              onChange={(e) => {
                setCloseDate(e.target.value);
              }}
              min={`${year}-${("0" + month).slice(-2)}-${date}`}
            />
            <span>까지</span>
          </InputWrapper>
          <InputWrapper>
            <span>여행지 </span>
            <input
              type="text"
              required
              onChange={(e) => {
                setLocation(e.target.value);
              }}
            />
          </InputWrapper>
          <InputWrapper>
            <span>본인을 포함한 총 모집 인원 수 </span>
            <input
              className="number"
              type="number"
              required
              onChange={(e) => {
                setTotalCount(e.target.value);
              }}
            />
          </InputWrapper>
          <Editor
            placeholder="내용을 입력해주세요."
            required
            ref={bodyRef}
            previewStyle="vertical" // 미리보기 스타일 지정
            height="300px" // 에디터 창 높이
            initialEditType="markdown" // 초기 입력모드 설정(디폴트 markdown)
            hideModeSwitch={true}
            toolbarItems={[
              // 툴바 옵션 설정
              ["heading", "bold", "italic", "strike"],
              ["hr", "quote"],
              ["ul", "ol", "task", "indent", "outdent"],
              ["table", "image", "link"],
              ["code", "codeblock"],
            ]}
            plugins={[colorSyntax]} // colorSyntax 플러그인 적용
          ></Editor>
          <Button
            onClick={() => {
              submitHandler();
            }}
          >
            작성 완료
          </Button>
          <Button
            onClick={() => {
              navigate(`/auth`);
            }}
          >
            취소
          </Button>
        </div>
      </div>
    </>
  );
};

export default NewPost;
const InputWrapper = styled.div`
  * {
    z-index: 0;
    margin: 0.5rem;
    .container {
      grid-template-columns: fit-content(300px) fit-content(300px);
    }
  }
  span {
    font-size: large;
  }
  .container {
    display: flex;
    flex-direction: column-reverse;
  }
  .input-tag {
    display: block;
    text-align: left;
    font-size: 1em;
  }
  #title {
    width: 20rem;
  }
  input {
    padding: 10px 5px;
    border-radius: 5px;
    outline: none;
    width: 10rem;
    display: inline-block;
    border: none;
    border-radius: 5px;
    margin-right: 10%;
    border: 1.5px solid #a19f9f;
  }
  label {
    font-size: large;
    line-height: normal;
    padding: 0.5rem;
  }
  #none + label {
    color: #a19f9f;
  }
  #hover:hover {
    border: 1.5px solid black;
  }
  #hover:hover + label {
    color: black;
  }
  #focus:focus + label {
    color: #2962ff;
  }
  #focus:focus {
    border: 1.5px solid #2962ff;
  }
`;