import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

// toast
import { Editor } from "@toast-ui/react-editor";
import "@toast-ui/editor/dist/toastui-editor.css";

// toast color syntax
import "tui-color-picker/dist/tui-color-picker.css";
import "@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";

const EditPost = () => {
  const { id } = useParams();
  const [editData, setEditData] = useState([]);
  const [title, setTitle] = useState(editData.title);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/posts/${id}`).then((res) => {
      console.log(res.data);
      setEditData(res.data);
    });
  }, [id]);
  return (
    <div>
      <div>
        <span>제목</span>
        <input
          type="text"
          value={editData.title}
          onChange={(e) => setTitle(e.target.value)}
        />
      </div>
      <div>
        <span>본인을 포함한 총 모집 인원 수 </span>
        <input type="number" required value={editData.totalCount} />
      </div>
      <div>
        <span>모집 마감 </span>
        <input type="date" required value={editData.closeDate}></input>
        <span> 까지</span>
      </div>
      {editData.body && (
        <Editor
          placeholder="내용을 입력해주세요."
          required
          previewStyle="vertical" // 미리보기 스타일 지정
          height="300px" // 에디터 창 높이
          initialValue={editData.body ? editData.body : ""}
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
    </div>
  );
};

export default EditPost;
