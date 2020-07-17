import { SortEvent } from "primeng/primeng";

export class UtilsGrids {
	/**
	 * Ordencao dos campos da grid 
	 * @param event 
	 */
	static customSort(event: SortEvent) {
		event.data.sort((data1, data2) => {
			let value1 = data1[event.field];
			let value2 = data2[event.field];
			let result = null;
			if (value1 == null && value2 != null) {
				result = -1;
			} else if (value1 != null && value2 == null) {
				result = 1;
			} else if (value1 == null && value2 == null) {
				result = 0;
			} else {
				result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;
			}
			return (event.order * result);
		});
	}
}