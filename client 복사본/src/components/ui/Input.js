import styled from "styled-components";

const Input = () => {
  return (
    <StyledInput className="input-tag">
      <div className="container">
        <input
          title="&lt; /&gt;"
          labl="Label"
          pholder="Placeholder"
          idn="none"
        />
        {/* <input
          title="&amp; : hover"
          labl="Label"
          pholder="Placeholder"
          idn="hover"
        />
        <input
          title="&amp; : focus"
          labl="Label"
          pholder="Placeholder"
          idn="focus"
        /> */}
      </div>
    </StyledInput>
  );
};
export default Input;
const StyledInput = styled.div`
  
`;
