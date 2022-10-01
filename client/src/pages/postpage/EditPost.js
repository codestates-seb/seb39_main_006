import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

// toast
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import Button from "../../components/ui/Button";
import Input from "../../components/ui/Input";

// toast color syntax
import "tui-color-picker/dist/tui-color-picker.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";

const EditPost = () => {
  const { id } = useParams();
  const [editData, setEditData] = useState([]);
  const [title, setTitle] = useState("");
  const [mate, setMate] = useState();
  const [body, setBody] = useState("");
  const [closeDate, setCloseDate] = useState("");
  const editBody = useRef();

  const navigate = useNavigate();

  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const date = today.getDate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/posts/${id}`).then((res) => {
      setEditData(res.data);
      setTitle(res.data.title);
      setMate(res.data.totalCount);
      setBody(res.data.body);
      setCloseDate(res.data.closeDate);
    });
  }, [id]);

  const submitEditDataHandler = () => {
    const enteredBody = editBody.current?.getInstance().getMarkdown();
    axios(`https://seb-006.shop/api/posts/${id}`, {
      method: "PATCH",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
      data: {
        title: title,
        body: enteredBody,
        totalCount: mate,
        closeDate: closeDate,
        images: [],
      },
    })
      .then((res) => {
        if (res.headers.access_hh) {
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
        }
        navigate(`/${id}`);
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
    <div>
      <div>
        <span>제목</span>
        <input
          type="text"
          required
          defaultValue={title}
          onChange={(e) => {
            setTitle(e.target.value);
          }}
        />
      </div>
      <div>
        <span>본인을 포함한 총 모집 인원 수 </span>
        <input
          type="number"
          required
          defaultValue={mate}
          onChange={(e) => {
            setMate(e.target.value);
          }}
        />
      </div>
      <div>
        <span>모집 마감 </span>
        <input
          type="date"
          required
          defaultValue={closeDate}
          onChange={(e) => {
            setCloseDate(e.target.value);
          }}
          min={`${year}-${("0" + month).slice(-2)}-${date}`}
        />
        <span> 까지</span>
      </div>
      {editData.body && (
        <Editor
          placeholder="내용을 입력해주세요."
          required
          ref={editBody}
          previewStyle="vertical" // 미리보기 스타일 지정
          height="300px" // 에디터 창 높이
          initialValue={body}
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
      )}
      {sessionStorage.getItem("userName") === editData.leaderName ? (
        <Button
          onClick={() => {
            submitEditDataHandler();
          }}
        >
          수정 완료
        </Button>
      ) : null}
      <Button
        onClick={() => {
          navigate(`/${id}`);
        }}
      >
        취소
      </Button>
    </div>
  );
};

export default EditPost;
