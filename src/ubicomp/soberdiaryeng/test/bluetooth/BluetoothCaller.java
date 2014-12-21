package ubicomp.soberdiaryeng.test.bluetooth;

import ubicomp.soberdiaryeng.test.Tester;

public interface BluetoothCaller extends Tester {
	public void stopDueToInit();

	public void failBT();

	public void setPairMessage();
}
