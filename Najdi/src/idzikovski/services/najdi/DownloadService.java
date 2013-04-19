package idzikovski.services.najdi;

import idzikovski.services.najdi.atasks.GetAllMestaTask;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		GetAllMestaTask task = new GetAllMestaTask(this);
		task.execute();

		return super.onStartCommand(intent, flags, startId);
	}

}
