import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

// Toast-UI Viewer 임포트
import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { Viewer } from "@toast-ui/react-editor";
import styled from "styled-components";

const PostDetail = () => {
  const { id } = useParams();
  const [detail, setDetail] = useState([]);
  const [matchList, setMatchList] = useState([]);
  const [matchBody, setMatchBody] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/posts/${id}`).then((res) => {
      setDetail(res.data);
    });
    axios(`https://seb-006.shop/api/posts/${id}/matching`).then((res) => {
      setMatchList(res.data.data);
    });
  }, [id]);

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
      <h1>{detail.title}</h1>
      {sessionStorage.getItem("userName") === detail.leaderName ? (
        <button
          onClick={() => {
            navigate(`/edit/${id}`);
          }}
        >
          게시글 수정
        </button>
      ) : null}
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
      {sessionStorage.getItem("userName") === detail.leaderName ? (
        <button
          onClick={() => {
            deleteHandler();
          }}
        >
          게시글을 삭제한다...!
        </button>
      ) : (
        <>
          {console.log(detail.postsStatus)}
          <div>
            <textarea
              onChange={(e) => {
                setMatchBody(e.target.value);
              }}
            ></textarea>
          </div>
          <button
            onClick={() => {
              matchSubmitHandler();
            }}
          >
            매칭 신청
          </button>
        </>
      )}
      <h2>매칭 신청들~~~</h2>
      {matchList.map((el, idx) => (
        <BorderContainer key={idx}>
          <span>
            <span>신청자 : {el.memberName} </span>
            {sessionStorage.getItem("userName") === detail.leaderName ? (
              <button
                onClick={() => {
                  navigate(`/match/${el.matchingId}`);
                }}
              >
                매칭관리
              </button>
            ) : null}
            {el.matchingStatus === "READ" ? <span>읽음</span> : null}
            {el.matchingStatus === "NOT_READ" ? <span>안읽음</span> : null}
          </span>
        </BorderContainer>
      ))}
      <h2>참여자 명단~~~</h2>
      {detail.participants &&
        detail.participants.map((el, idx) => (
          <BorderContainer key={idx}>
            <span>
              <div>닉네임 : {el.displayName} </div>
              <div>자기소개 : {el.content}</div>
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
            </span>
          </BorderContainer>
        ))}
    </div>
  );
};

export default PostDetail;

const BorderContainer = styled.div`
  border: 1px solid black;
`;
