import ShinyText from "@/components/ui/shiny-text";
import { ReactElement } from "react";

const ShinyLoadingText = ({ text }: { text: string }): ReactElement => (
    <ShinyText
        text={text}
        speed={1.5}
        delay={0}
        color="#b5b5b5"
        shineColor="#ffffff"
        spread={35}
        direction="left"
    />
);
export default ShinyLoadingText;
