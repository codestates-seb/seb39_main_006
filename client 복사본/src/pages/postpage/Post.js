import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
const Post = ({ datas }) => {
  const navigate = useNavigate();
  const [isbookmark, setIsBookmark] = useState(false);
  const [mybookmark, setMyBookmark] = useState([]);

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/members/my-bookmark`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    })
      .then((res) => {
        setMyBookmark(res.data.postIds);
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
        } else if (err.response.status === 0)
          alert(
            "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
          );
        else {
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

  useEffect(() => {
    mybookmark.map((el) => (el === post.postId ? setIsBookmark(true) : null));
  }, [mybookmark, post.postId]);

  const bookmarkHandler = () => {
    setIsBookmark(!isbookmark);
    axios(
      `${process.env.REACT_APP_URL}/api/members/bookmark?postId=${post.postId}`,
      {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      }
    ).catch((err) => {
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
      } else if (err.response.status === 0)
        alert(
          "서버 오류로 인해 불러올 수 없습니다. 조금 뒤에 다시 시도해주세요"
        );
      else {
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
  };

  return (
    <div>
      <PostTitle>
        <span
          onClick={() => {
            navigate(`/${post.postId}`);
          }}
        >
          {post.title}
        </span>
        <span>
          <span className="participants">
            <span>모집 인원 </span>
            <span>
              {post.participantsCount} / {post.totalCount}
            </span>
          </span>
          <button
            onClick={() => {
              bookmarkHandler();
            }}
          >
            {isbookmark ? "❤️" : "🤍"}
          </button>
        </span>
      </PostTitle>
    </div>
  );
};

export default Post;

const PostTitle = styled.div`


  
`;
