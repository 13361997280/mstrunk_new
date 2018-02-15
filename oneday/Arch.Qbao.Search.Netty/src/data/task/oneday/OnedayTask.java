package data.task.oneday;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * oneday  项目任务
 *
 * @author song.j
 * @create 2017-09-14 09:09:24
 **/
@Deprecated
public class OnedayTask implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("脚本执行");
    }

    public static void main(String[] args) {
        OnedayDataBase onedayDataBase =new OnedayDataBase();
        onedayDataBase.start();
    }
}
