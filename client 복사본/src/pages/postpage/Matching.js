import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";

const Matching = () => {
  const { matchid } = useParams();
  const [matchData, setMatchData] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/matching/${matchid}`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    }).then((res) => {
      setMatchData(res.data);
    });
  }, [matchid]);

  const acceptHandler = () => {
    axios(`${process.env.REACT_APP_URL}/api/matching/${matchid}/accept`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    })
      .then(() => {
        navigate(`/${matchData.postId}`);
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
          } else if (
            err.response.data.korMessage === "매칭 정보를 찾을 수 없습니다."
          ) {
            alert(err.response.data.korMessage);
            navigate(`/${matchData.postId}`);
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

  const refuseHandler = () => {
    axios(`${process.env.REACT_APP_URL}/api/matching/${matchid}/refuse`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
      },
    })
      .then(() => {
        navigate(`/${matchData.postId}`);
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
          } else if (
            err.response.data.korMessage === "매칭 정보를 찾을 수 없습니다."
          ) {
            alert(err.response.data.korMessage);
            navigate(`/${matchData.postId}`);
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
    <PageContainer>
      <Container>
        <ContentWrap>
          <p>매칭 게시글 :</p>
          <h1 className="title"> {matchData.postTitle}</h1>
          <p>매칭 신청자 : </p>
          <div className="person">{matchData.memberName}</div>
          <p>매칭 내용 :</p>
          <div className="contents"></div>
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
        </ContentWrap>
      </Container>
    </PageContainer>
  );
};

export default Matching;
const PageContainer = styled.div`
  
`;
const Container = styled.div`
  
`;
const ContentWrap = styled.div`
  
`;
const MatchBody = styled.div`
 
`;

const Button = styled.button`
  
`;
