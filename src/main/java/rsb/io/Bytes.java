package rsb.io;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Bytes {

	private final byte[] bytes;

	private final int length;

	public static Bytes from(byte[] bytes, int len) {
		return new Bytes(bytes, len);
	}

}
