"use client";

import { useCallback, useEffect, useState } from "react";

type ScrolledResponse = {
    scrolled: boolean;
};

export const useScrolled = (threshold = 15): ScrolledResponse => {
    const [scrolled, setScrolled] = useState<boolean>(false);

    const handleScroll = useCallback(() => {
        const isScrolled = window.scrollY > threshold;
        if (isScrolled !== scrolled) {
            setScrolled(isScrolled);
        }
    }, [threshold, scrolled]);

    useEffect(() => {
        let ticking = false;
        let lastScrollY = window.scrollY;

        const throttledScroll = () => {
            if (!ticking) {
                window.requestAnimationFrame(() => {
                    // Only process if we've actually scrolled
                    if (lastScrollY !== window.scrollY) {
                        handleScroll();
                        lastScrollY = window.scrollY;
                    }
                    ticking = false;
                });
                ticking = true;
            }
        };

        handleScroll(); // Initial check
        window.addEventListener("scroll", throttledScroll, { passive: true });
        return () => window.removeEventListener("scroll", throttledScroll);
    }, [handleScroll]);

    return { scrolled };
};