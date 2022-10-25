import SideBar from "../../components/SideBar";
import styled from "styled-components";
import H1 from "../../components/ui/H1";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const MyPage = () => {
  const [userInfo, setUserInfo] = useState("");
  const memberId = sessionStorage.getItem("memberId");
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_URL}/api/members/${memberId}`, {
        headers: {
          access_hh: sessionStorage.getItem("AccessToken"),
        },
      })
      .then((res) => {
        setUserInfo(res.data);
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

  return (
    <>
      <SideBar />
      <Section>
        <section>
          <H1>My Page</H1>
          <h2>여행동행자 모집합니다</h2>
          <PageContainer>
            <ContainerWrap>
              <section className="userinfo">
                <a href="/userinfo">
                  <button>유저정보 수정하기</button>
                </a>
              </section>
              <div className="label-wrapper">
                <label htmlFor="displayName">User Name</label>
                <div className="container">
                  <input
                    readOnly
                    className="input-tag"
                    defaultValue={userInfo !== null ? userInfo.displayName : ""}
                  ></input>
                </div>

                <label htmlFor="phone">Phone Number</label>
                <div className="container">
                  <input
                    readOnly
                    className="input-tag"
                    defaultValue={userInfo !== null ? userInfo.phone : ""}
                  ></input>
                </div>

                <label htmlFor="content">자기소개</label>
                <div className="container">
                  <input
                    id="iamground"
                    readOnly
                    className="input-tag"
                    type="textarea"
                    defaultValue={userInfo !== null ? userInfo.content : ""}
                  ></input>
                </div>

                <label htmlFor="profile-img">프로필 사진</label>
                <div className="container">
                  <img
                    src={userInfo !== null ? userInfo.profileImage : ""}
                    width="100"
                    height="100"
                  ></img>
                </div>
              </div>
            </ContainerWrap>
          </PageContainer>
        </section>
      </Section>
    </>
  );
};
export default MyPage;
const PageContainer = styled.div`
  
`;
const ContainerWrap = styled.div`
  
`;

const Section = styled.div`
 
`;
