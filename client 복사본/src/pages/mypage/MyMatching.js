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
    })
      .then((res) => {
        setMyMatchingInfo(res.data.data);
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
      <div className="wrapper">
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
      </div>
    </StyledDiv>
  );
};

export default MyMatching;
const StyledDiv = styled.div`
  
`;
