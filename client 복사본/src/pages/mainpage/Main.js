import MainPage from "./MainPage";
import MainSubHeader from "./MainSubHeader";
import styled from "styled-components";

const Main = () => {
  return (
    <StyledDiv>
    <main>
      <div >
        <MainSubHeader />
      </div>
      <div >
        <MainPage />
      </div>
     
        </main>
    </StyledDiv>
  );
};
export default Main;

const StyledDiv = styled.div`

`;
