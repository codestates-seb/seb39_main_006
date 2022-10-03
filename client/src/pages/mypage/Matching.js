import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";

const Matching = () => {
  const { matchid } = useParams();
  const [matchData, setMatchData] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`https://seb-006.shop/api/matching/${matchid}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then((res) => {
      setMatchData(res.data);
    });
  }, [matchid]);

  const acceptHandler = () => {
    axios(`https://seb-006.shop/api/matching/${matchid}/accept`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then(() => {
      navigate(`/${matchData.postId}`);
    });
  };

  const refuseHandler = () => {
    axios(`https://seb-006.shop/api/matching/${matchid}/refuse`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then(() => {
      navigate(`/${matchData.postId}`);
    });
  };

  return (
    <div>
      <h1>매칭 게시글 : {matchData.postTitle}</h1>
      <div>매칭 신청자 : {matchData.memberName}</div>
      <div>매칭 내용</div>
      <MatchBody>{matchData.body}</MatchBody>
      <span>
        <Button
          onClick={() => {
            acceptHandler();
          }}
        >
          수락
        </Button>
        <Button
          onClick={() => {
            refuseHandler();
          }}
        >
          거절
        </Button>
      </span>
    </div>
  );
};

export default Matching;

const MatchBody = styled.div`
  background-color: #d5eaf1;
  width: 70%;
  height: 30vh;
`;

const Button = styled.button`
  font-size: 1rem;
  background-color: #dabbc9;
  width: fit-content;
  border: 1px solid #dabbc9;
  padding: 0.5rem 1rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
  color: #425049;
  &:hover {
    background-color: #efd5c8;
    border-color: #efd5c8;
  }
`;
