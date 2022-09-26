import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const PostDetail = () => {
  const { id } = useParams();
  const [detail, setDetail] = useState([]);

  useEffect(() => {
    axios(`https://seb-006.shop/api/posts/${id}`).then((res) => {
      setDetail(res.data);
    });
  }, []);
  return (
    <div>
      <h1>{detail.title}</h1>
      <button>게시글 수정</button>
      <div>작성자 : {detail.leaderName}</div>
      <div>
        여행일정 : {detail.startDate} ~ {detail.endDate}
      </div>
      <div>여행지역 : {detail.location}</div>
      <div>매칭기간 : {detail.closeDate} 까지</div>
      <div>
        모집 인원 {detail.participantsCount} / {detail.totalCount}
      </div>
      <h3>{detail.body}</h3>
    </div>
  );
};

export default PostDetail;
