import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SideBar from "../../components/SideBar";
import H1 from "../../components/ui/H1";
import styled from "styled-components";
const MyMatching = () => {
  const [myMatchingInfo, setMyMatchingInfo] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/members/matching`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      setMyMatchingInfo(res.data.data);
    });
  }, []);

  const matchStatus = (status) => {
    if (status === "READ") return "읽음";
    if (status === "NOT_READ") return "안읽음";
    if (status === "ACCEPTED") return "수락";
    if (status === "REFUSED") return "거절";
  };

  return (
    <StyledDiv>
      <SideBar />

      <H1>매칭 신청한 게시글</H1>
      {myMatchingInfo.map((el, idx) => (
        <div key={idx}>
          <h2
            onClick={() => {
              navigate(`/${el.postId}`);
            }}
          >
            {el.postTitle} {matchStatus(el.matchingStatus)}
          </h2>
        </div>
      ))}
    </StyledDiv>
  );
};

export default MyMatching;
const StyledDiv = styled.div`
  .h1-wrap {
  }
`;
