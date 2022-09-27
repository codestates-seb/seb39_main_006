import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

// Toast-UI Viewer 임포트
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { Viewer } from "@toast-ui/react-editor";

const PostDetail = () => {
  const { id } = useParams();
  const [detail, setDetail] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/posts/${id}`).then((res) => {
      setDetail(res.data);
    });
  }, [id]);

  const deleteHandler = () => {
    axios(`https://seb-006.shop/api/posts/${id}`, {
      method: "DELETE",
      headers: {
        access_hh: sessionStorage.getItem("AccesToken"),
      },
    });
    navigate(`/auth`);
  };

  return (
    <div>
      <h1>{detail.title}</h1>
      <button
        onClick={() => {
          navigate(`/edit/${id}`);
        }}
      >
        게시글 수정
      </button>
      <div>작성자 : {detail.leaderName}</div>
      <div>
        여행일정 : {detail.startDate} ~ {detail.endDate}
      </div>
      <div>여행지역 : {detail.location}</div>
      <div>매칭기간 : {detail.closeDate} 까지</div>
      <div>
        모집 인원 {detail.participantsCount} / {detail.totalCount}
      </div>
      {detail.body && (
        <>
          <h2>본문</h2>
          <Viewer initialValue={detail.body} />
        </>
      )}
      <button
        onClick={() => {
          deleteHandler();
        }}
      >
        게시글을 삭제한다...!
      </button>
    </div>
  );
};

export default PostDetail;
