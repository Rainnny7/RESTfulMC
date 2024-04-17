import Counter from "@/components/counter";

/**
 * The statistic counters component.
 *
 * @returns the counters jsx
 */
const StatisticCounters = (): JSX.Element => (
	<div className="py-56 flex justify-center items-center">
		<div className="grid grid-flow-row grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-24">
			<Counter name="Testing" amount={1_000_000} />
			<Counter name="Testing" amount={1_000_000} />
			<Counter name="Testing" amount={1_000_000} />
			<Counter name="Testing" amount={1_000_000} />
		</div>
	</div>
);
export default StatisticCounters;
