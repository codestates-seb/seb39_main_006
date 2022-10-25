import { arrayOf } from "prop-types";
import { useState } from "react";
import React from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import { aboutdata } from "../../components/data/data";
const MainPage = () => {
  const [isActive, setIsActive] = useState(false);
  const boxOpen = () => {
    isActive === true ? setIsActive(false) : setIsActive(true);
  };

  return (
    <Section>
      <Layout>
        <Container>
          <body>
            <div class="container ">
              <nav>
                <h4> 네비게이션</h4>

                <ul>
                  <div>사이드 빈 칸</div>
                </ul>
              </nav>
              <main>
                <section>
                  {aboutdata.datas.map((el) => (
                    <div className="content">
                      <div className="title-wrapper">
                        <div id="profile"></div>
                        <h4>
                          <Link to={`/detail/${el.title}`}>제목입니다</Link>
                        </h4>
                      </div>
                      <img className="picture" src={el.pic} alt="travelDesc" />
                      <div className="btns">
                        <div>
                          <div className="button">
                            <button class="material-icons">favorite</button>
                            <button class="material-icons">send</button>
                            <button class="material-icons">
                              chat_bubble_outline
                            </button>
                            <button>...</button>
                            <div>
                              <button class="material-icons">
                                bookmark_border
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                      <span onClick={boxOpen}>
                        {isActive === true ? (
                          <span className="click">
                            <div>
                              <div>여행일정</div>
                              <div>
                                <span>{el.date}</span>
                              </div>
                            </div>
                            <div>
                              <div>모집인원</div>
                              <div>
                                <span>{el.person}</span>
                              </div>
                            </div>
                            <div>
                              <div>매칭기간</div>
                              <div>
                                <span>{el.matching}</span>
                              </div>
                            </div>
                          </span>
                        ) : (
                          <span className="total-span">
                            <div>
                              <div>여행일정</div>
                              <div>
                                <span>{el.date}</span>
                              </div>
                            </div>
                            <div>
                              <div>모집인원</div>
                              <div>
                                <span>{el.person}</span>
                              </div>
                            </div>
                            <div>
                              <div>매칭기간</div>
                              <div>
                                <span>{el.matching}</span>
                              </div>
                            </div>
                          </span>
                        )}
                      </span>
                    </div>
                  ))}
                </section>
              </main>
              <aside>
                <h4>aside 빈 칸</h4>
              </aside>
            </div>
            <footer>
              <ul>
                <li>개인정보 처리방침</li>
                <li>이용 약관</li>
                <li>법적 고지</li>
              </ul>
            </footer>
          </body>
        </Container>
      </Layout>
    </Section>
  );
};

export default MainPage;
const Layout = styled.div`
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  height: max-content;
  overflow: scroll;
  ::-webkit-scrollbar {
    margin: 0 auto;
    display: none;
  }
  @media screen and (max-width: 575px) {
  }
  // 가로모드 모바일 디바이스 (가로 해상도가 768px 보다 작은 화면에 적용)
  @media screen and (max-width: 768px) {
    width: 768px;
  }
  // 태블릿 디바이스 (가로 해상도가 992px 보다 작은 화면에 적용)
  @media screen and (max-width: 991px) {
  }
`;
const Container = styled.div`
  // 기본 CSS를 작성합니다.
  // 기본으로 작성되는 CSS는 1199px보다 큰 화면에서 작동 됩니다.
  // 세로모드 모바일 디바이스 (가로 해상도가 576px 보다 작은 화면에 적용)
  section {
    display: flex;
    flex-wrap: wrap;

    .content {
      display: inline-block;
      background: #a2c9c6;
      width: 250px;
      height: fit-content;
      margin: 5px;
    }
  }
  flex-direction: column;
  body {
    max-width: 1200px;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }
  .container {
    display: flex;
    flex: 1;
  }

  .picture {
    display: inline-block;
    width: 250px;
    height: 175px;
    background-color: beige;
  }
  .button {
    display: flex;
    flex: 1;
    justify-content: space-around;
    margin: 5px 0 5px 0;
  }

  .total-span {
    display: inline-block;
    max-width: 80%;
    height: 3rem;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }

  #profile {
    width: 50px;
    height: 50px;
    border-radius: 25px;
    background-color: #777;
  }
  .title-wrapper {
    padding: 10px;
  }
  main {
    display: flex;
    flex-wrap: wrap;
    padding: 0 20px;
  }
  nav {
    flex: 0 0 50px;
    padding: 0 10px;
  }
  aside {
    flex: 0 0 20px;
    padding: 0 10px;
  }
`;
const Section = styled.div`
  .material-icons {
    font-family: "Material Icons";
    font-weight: normal;
    font-style: normal;
    font-size: 24px; /* Preferred icon size */
    display: inline-block;
    line-height: 1;
    text-transform: none;
    letter-spacing: normal;
    word-wrap: normal;
    white-space: nowrap;
    direction: ltr;

    /* Support for all WebKit browsers. */
    -webkit-font-smoothing: antialiased;
    /* Support for Safari and Chrome. */
    text-rendering: optimizeLegibility;

    /* Support for Firefox. */
    -moz-osx-font-smoothing: grayscale;

    /* Support for IE. */
    font-feature-settings: "liga";
  }
  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    list-style: none;
    font-family: "Noto Sans KR", sans-serif;
  }
`;
