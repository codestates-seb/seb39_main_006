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
      },
    })
      .then((res) => {
        setMypost(res.data.data);
      })
      .catch((err) => {
        if (err.response.status === 400) {
          if (err.response.data.fieldErrors) {
            alert(err.response.data.fieldErrors[0].reason);
          } else if (
            err.response.data.fieldErrors === null &&
            err.response.data.violationErrors
          ) {
            alert(err.response.data.violationErrors[0].reason);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        } else {
          if (
            err.response.data.korMessage ===
            "만료된 토큰입니다. 다시 로그인 해주세요."
          ) {
            sessionStorage.clear();
            navigate(`/`);
            window.location.reload();
          } else if (err.response.data.korMessage) {
            alert(err.response.data.korMessage);
          } else {
            alert(
              "우리도 무슨 오류인지 모르겠어요... 새로고침하고 다시 시도해주세요.... 미안합니다.....ㅠ"
            );
          }
        }
        window.location.reload();
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
