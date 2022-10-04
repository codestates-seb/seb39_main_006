import styled from "styled-components";

const StyledBtn = styled.button`
  display: grid;
  grid: auto / repeat(auto-fit, minmax(200px, auto));
  place-items: center;
  font-size: 1.25rem;
  background-color: #dabbc9;
  width: fit-content;
  border: 1px solid #dabbc9;
  padding: 0.5rem 1rem;
  margin: 0.5rem;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
  color: #425049;
  &:hover {
    background-color: #efd5c8;
    border-color: #efd5c8;
  }
`;

export default StyledBtn;
