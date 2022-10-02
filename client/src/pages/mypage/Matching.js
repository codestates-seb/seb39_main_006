import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

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
      <div>{matchData.body}</div>
      <span>
        <button
          onClick={() => {
            acceptHandler();
          }}
        >
          수락
        </button>
        <button
          onClick={() => {
            refuseHandler();
          }}
        >
          거절
        </button>
      </span>
    </div>
  );
};

export default Matching;
