import SideBar from "../../components/SideBar";
import styled from "styled-components";
import Button from "../../components/ui/Button";
import WrapperBox from "../../components/ui/WrapperBox";
import H1 from "../../components/ui/H1";
const MyPage = () => {
  return (
    <>
      <SideBar />
      <Section>
        <section>
          <H1>My Page</H1>
          <h2>여행동행자 모집합니다</h2>
          <section>
            <a href="/userinfo">
              <Button>유저정보 수정하기</Button>
            </a>
          </section>

          <WrapperBox></WrapperBox>
        </section>
      </Section>
    </>
  );
};
export default MyPage;

const Section = styled.div`
  section {
    margin-top: 2rem;
    display: flex;
    position: relative;
    left: 100px;
    list-style: none;
    section {
      display: inline-block;
      margin: 3rem;
    }
  }

  h1 {
    font-size: 52px;
    display: block;
    align-items: center;
    font-family: "Montserrat", sans-serif;
    text-transform: uppercase;
    text-shadow: 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9, 3px 3px 0px #dabbc9,
      4px 4px 0px #dabbc9;
  }
  h2 {
    padding: 1rem;
    font-size: 15px;
    display: block;
    align-items: center;
    font-family: "Montserrat", sans-serif;
    text-transform: uppercase;
    text-shadow: 1px 1px 0px #dabbc9, 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9,
      3px 3px 0px #dabbc9;
  }
`;
