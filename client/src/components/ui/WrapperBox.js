import styled from "styled-components";

export default function WrapperBox() {
  return (
    <Box>
      <ContentsBox></ContentsBox>
    </Box>
  );
}
const Box = styled.div`
  z-index: -888;
  width: 700px;
  height: 700px;
  background-color: #d5eaf1;
  border-radius: 10px;
  position: absolute;
  box-shadow: 0px 3px 10px 1px rgba(0, 0, 0, 0.3);
`;

const ContentsBox = styled.div`
  z-index: -777;
  padding: 3.5em;
  position: relative;
  height: 65%;
`;
