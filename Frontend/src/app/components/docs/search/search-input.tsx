import { ReactElement } from "react";
import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import SearchDialogContent from "@/components/docs/search/search-dialog";

/**
 * The quick search component.
 *
 * @return the search jsx
 */
const QuickSearch = (): ReactElement => (
    <Dialog>
        <DialogTrigger>
            {/* Button to open search */}
            <div className="absolute top-2.5 left-3 z-10">
                <MagnifyingGlassIcon
                    className="absolute"
                    width={22}
                    height={22}
                />
            </div>

            <Input
                className="pl-10"
                type="search"
                name="search"
                placeholder="Quick search..."
                readOnly
            />
        </DialogTrigger>
        <DialogContent>
            <SearchDialogContent />
        </DialogContent>
    </Dialog>
);
export default QuickSearch;
