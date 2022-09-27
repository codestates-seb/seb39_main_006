import React, { useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
// toast
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";

// toast color syntax
import "tui-color-picker/dist/tui-color-picker.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";

const NewPost = () => {
  const navigate = useNavigate();

  const titleRef = useRef();
  const bodyRef = useRef();
  const startDateRef = useRef();
  const endDateRef = useRef();
  const locationRef = useRef();
  const totalCountRef = useRef();
  const closeDateRef = useRef();

  const submitHandle = () => {
    const enteredTitle = titleRef.current.value;
    const enteredBody = bodyRef.current?.getInstance().getHTML();
    const enteredStartDate = startDateRef.current.value;
    const enteredEndDate = endDateRef.current.value;
    const enteredLocation = locationRef.current.value;
    const enteredTotalCount = totalCountRef.current.value;
    const enteredCloseDate = closeDateRef.current.value;

    console.log("enteredTitle = " + enteredTitle);
    console.log("enteredBody = " + enteredBody);
    console.log("enteredBodytype = " + typeof enteredBody);
    console.log("enteredStartDate = " + enteredStartDate);
    console.log("enteredEndDate = " + enteredEndDate);
    console.log("enteredLocation = " + enteredLocation);
    console.log("enteredTotalCount = " + enteredTotalCount);
    console.log("enteredCloseDate = " + enteredCloseDate);

    axios(`https://seb-006.shop/api/posts`, {
      method: "POST",
      headers: { access_hh: sessionStorage.getItem("AccesToken") },
      data: {
        title: enteredTitle,
        body: enteredBody,
        startDate: enteredStartDate,
        endDate: enteredEndDate,
        location: enteredLocation,
        totalCount: enteredTotalCount,
        closeDate: enteredCloseDate,
        images: [],
      },
    }).then((res) => console.log(res));
  };

  return (
    <div>
      <div>
        <span>제목 </span>
        <input type="text" required ref={titleRef} />
      </div>
      <div>
        <span>여행 출발 날짜 </span>
        <input type="date" required ref={startDateRef}></input>
      </div>
      <div>
        <span>여행 종료? 날짜 </span>
        <input type="date" required ref={endDateRef}></input>
      </div>
      <div>
        <span>모집 마감 </span>
        <input type="date" required ref={closeDateRef}></input>
        <span> 까지</span>
      </div>
      <div>
        <span>여행지 </span>
        <input type="text" required ref={locationRef}></input>
      </div>
      <div>
        <span>본인을 포함한 총 모집 인원 수 </span>
        <input type="number" required ref={totalCountRef}></input>
      </div>
      <Editor
        placeholder="내용을 입력해주세요."
        required
        ref={bodyRef}
        previewStyle="vertical" // 미리보기 스타일 지정
        height="300px" // 에디터 창 높이
        initialEditType="markdown" // 초기 입력모드 설정(디폴트 markdown)
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
      <button
        onClick={() => {
          // navigate(`/auth`);
          submitHandle();
        }}
      >
        작성 완료
      </button>
      <button>취소</button>
    </div>
  );
};

export default NewPost;
