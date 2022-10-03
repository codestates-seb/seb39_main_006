import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";

// Toast-UI Viewer 임포트
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { Viewer } from "@toast-ui/react-editor";

const PostDetail = () => {
  const { id } = useParams();
  const [detail, setDetail] = useState([]);
  const [matchList, setMatchList] = useState([]);
  const [matchBody, setMatchBody] = useState("");
  const [isbookmark, setIsBookmark] = useState(false);
  const [mybookmark, setMyBookmark] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/posts/${id}`).then((res) => {
      setDetail(res.data);
    });
    axios(`https://seb-006.shop/api/posts/${id}/matching`).then((res) => {
      setMatchList(res.data.data);
    });
  }, [id]);

  useEffect(() => {
    axios(`https://seb-006.shop/api/members/my-bookmark`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then((res) => {
      setMyBookmark(res.data.postIds);
    });
  }, []);

  useEffect(() => {
    mybookmark.map((el) => (el === detail.postId ? setIsBookmark(true) : null));
  }, [mybookmark, detail.postId]);

  const bookmarkHandler = () => {
    setIsBookmark(!isbookmark);
    axios(`https://seb-006.shop/api/members/bookmark?postId=${detail.postId}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    });
  };

  const deleteHandler = () => {
    axios(`https://seb-006.shop/api/posts/${id}`, {
      method: "DELETE",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    })
      .then((res) => {
        if (res.headers.access_hh) {
          sessionStorage.setItem("AccessToken", res.headers.access_hh);
        }
        navigate(`/main`);
        window.location.reload();
      })
      .catch((err) => {
        if (err.response.status === 500) {
          alert("세션이 만료되어 로그아웃합니다.");
          sessionStorage.clear();
          navigate(`/`);
          window.location.reload();
        }
        console.log(err);
      });
  };

  const matchSubmitHandler = () => {
    axios(`https://seb-006.shop/api/matching/posts/${id}`, {
      method: "POST",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
      data: { body: matchBody },
    }).then(() => {
      window.location.reload();
    });
  };

  const goAway = (memberPostId) => {
    axios(`https://seb-006.shop/api/participants/${memberPostId}`, {
      method: "DELETE",
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then(() => {
      window.location.reload();
    });
  };

  return (
    <div>
      <h1>
        {detail.title}
        {sessionStorage.getItem("userName") === detail.leaderName ? (
          <>
            <button
              onClick={() => {
                navigate(`/edit/${id}`);
              }}
            >
              게시글 수정
            </button>
            <button
              onClick={() => {
                deleteHandler();
              }}
            >
              게시글을 삭제
            </button>
          </>
        ) : null}
        <button
          onClick={() => {
            bookmarkHandler();
          }}
        >
          {isbookmark ? "❤️" : "🤍"}
        </button>
      </h1>
      <div>작성자 : {detail.leaderName}</div>
      <Container>
        <div>
          <FlexContainer>
            <span className="flexbody">
              <span>여행일정</span>
              <span>
                {detail.startDate} ~ {detail.endDate}
              </span>
            </span>
            <span className="flexbody">
              <span>여행지역</span>
              <span>{detail.location}</span>
            </span>
            <span className="flexbody">
              <span>매칭기간</span>
              <span>{detail.closeDate} 까지</span>
            </span>
            <span className="flexbody">
              <span>모집 인원</span>
              <span>
                {detail.participantsCount} / {detail.totalCount}
              </span>
            </span>
          </FlexContainer>
          {detail.body && (
            <div>
              <h2>본문</h2>
              <BodyContainer>
                <Viewer initialValue={detail.body} />
              </BodyContainer>
            </div>
          )}
          {sessionStorage.getItem("userName") === detail.leaderName ? null : (
            <Matchtext>
              <textarea
                onChange={(e) => {
                  setMatchBody(e.target.value);
                }}
              ></textarea>
              <button
                onClick={() => {
                  matchSubmitHandler();
                }}
              >
                매칭 신청
              </button>
            </Matchtext>
          )}
        </div>

        <div>
          <h2>매칭 신청</h2>
          {matchList.map((el, idx) => (
            <Match key={idx}>
              <span>신청자 : {el.memberName} </span>
              <span className="isread">
                {sessionStorage.getItem("userName") === detail.leaderName ? (
                  <button
                    onClick={() => {
                      navigate(`/match/${el.matchingId}`);
                    }}
                  >
                    매칭관리
                  </button>
                ) : null}
                {el.matchingStatus === "READ" ? <span>✅</span> : null}
                {el.matchingStatus === "NOT_READ" ? <span>❌</span> : null}
              </span>
            </Match>
          ))}
          <h2>참여자 명단</h2>
          {detail.participants &&
            detail.participants.map((el, idx) => (
              <Match key={idx}>
                <span>
                  <div>
                    닉네임 : {el.displayName}
                    {sessionStorage.getItem("userName") === detail.leaderName &&
                    sessionStorage.getItem("userName") !== el.displayName ? (
                      <button
                        onClick={() => {
                          goAway(el.memberPostId);
                        }}
                      >
                        여행 추방
                      </button>
                    ) : null}
                    {sessionStorage.getItem("userName") !== detail.leaderName &&
                    sessionStorage.getItem("userName") === el.displayName ? (
                      <button
                        onClick={() => {
                          goAway(el.memberPostId);
                        }}
                      >
                        참여 취소
                      </button>
                    ) : null}
                  </div>
                  <div>자기소개 : {el.content}</div>
                </span>
              </Match>
            ))}
        </div>
      </Container>
    </div>
  );
};

export default PostDetail;

const Match = styled.div`
  display: flex;
  justify-content: space-between;
  border: 1px solid black;
  background-color: #d5eaf1;
  margin-top: 10px;
  margin-bottom: 10px;
  padding: 5px;
  border-radius: 5px;
  .isread {
    margin-right: 10px;
  }
  button {
    margin-left: 5px;
    margin-right: 5px;
  }
`;

const FlexContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-around;
  .flexbody {
    border: 1px solid black;
    border-radius: 15px;
    background-color: #d5eaf1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 25%;
    margin: 0.5rem;
  }
`;

const BodyContainer = styled.div`
  border: 1px solid black;
  background-color: #d5eaf1;
  border-radius: 15px;
  width: 70vw;
  .toastui-editor-contents {
    padding: 10px;
  }
  .toastui-editor-contents p {
    font-size: 17px;
  }
  margin-bottom: 20px;
`;

const Container = styled.div`
  display: flex;
  justify-content: space-around;
  /* background-color: yellow; */
  height: 80vh;
`;

const Matchtext = styled.div`
  display: flex;
  textarea {
    flex-grow: 1;
    font-size: 17px;
    height: 200px;
    margin-right: 20px;
  }
`;
