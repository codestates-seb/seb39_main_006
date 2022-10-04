import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SideBar from "../../components/SideBar";
import H1 from "../../components/ui/H1";
const MyPost = () => {
  const [myPost, setMypost] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/members/posts`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then((res) => {
      setMypost(res.data.data);
    });
  }, []);
  return (
    <div>
      <SideBar />
      <H1>내가 쓴 게시글</H1>
      {myPost.map((el, idx) => (
        <div key={idx}>
          <h2
            onClick={() => {
              navigate(`/${el.postId}`);
            }}
          >
            {el.title} 모집인원 {el.participantsCount} / {el.totalCount}
          </h2>
        </div>
      ))}
    </div>
  );
};

export default MyPost;
