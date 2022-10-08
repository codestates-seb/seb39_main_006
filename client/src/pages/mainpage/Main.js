import MainPage from "./MainPage";
import MainSubHeader from "./MainSubHeader";
import styled from "styled-components";

const Main = () => {
  return (
    <StyledDiv>
      <div className="wrapper">
        <MainSubHeader />
      </div>
      <div className="wrapper">
        <MainPage />
      </div>
    </StyledDiv>
  );
};
export default Main;

const StyledDiv = styled.div`
  .wrapper {
    margin: 0 auto;
  }
`;
