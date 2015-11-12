package org.jboss.reddeer.core.condition;

import org.eclipse.core.runtime.jobs.Job;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.logging.Logger;

/**
 * Condition is met when there is/are running non-system job(s).
 * List of jobs can be filtered using matchers.
 * 
 * @author Lucia Jelinkova
 */
@SuppressWarnings("rawtypes")
public class JobIsRunning extends AbstractWaitCondition {
	private static final Logger log = Logger.getLogger(JobIsRunning.class);

	private Matcher[] consideredJobs;
	private Matcher[] excludeJobs;
	private boolean skipSystemJobs;
	private Job[] currentJobs;

	/**
	 * Constructs JobIsRunning wait condition. Condition is met when job is running.
	 */
	public JobIsRunning() {
		this(null, null);
	}

	/**
	 * Constructs JobIsRunning wait condition. Condition is met when job(s) is/are running.
	 * Test only jobs matching the specified matchers.
	 * 
	 * @param consideredJobs If not <code>null</code>, only jobs whose name matches
	 * any of these matchers will be tested. Use in case you want to make sure all
	 * jobs from a limited set are not running, and you don't care about the rest
	 * of jobs.
	 */
	public JobIsRunning(Matcher[] consideredJobs) {
		this(consideredJobs, null);
	}

	/**
	 * Constructs JobIsRunning wait condition. Condition is met when job(s) is/are running.
	 * Test only jobs matching the specified matchers which are not excluded by 
	 * another specified matchers.
	 * 
	 * @param consideredJobs If not <code>null</code>, only jobs whose name matches
	 * any of these matchers will be tested. Use in case you want to make sure all
	 * jobs from a limited set are not running, and you don't care about the rest
	 * of jobs.
	 * @param excludeJobs If not <code>null</code>, jobs whose name matches any of
	 * these matcher will be ignored. Use in case you don't care about limited set
	 * of jobs. These matchers will overrule <code>consideredJobs</code> results,
	 * job matched by both <code>consideredJobs</code> and <code>excludeJobs</code>
	 * will be excluded.
	 */
	public JobIsRunning(Matcher[] consideredJobs, Matcher[] excludeJobs) {
		this(consideredJobs, excludeJobs, true);
	}

	/**
	 * Constructs JobIsRunning wait condition. Condition is met when job(s) is/are running.
	 * Test only jobs matching the specified matchers which are not excluded by 
	 * another specified matchers.
	 * 
	 * @param consideredJobs If not <code>null</code>, only jobs whose name matches
	 * any of these matchers will be tested. Use in case you want to make sure all
	 * jobs from a limited set are not running, and you don't care about the rest
	 * of jobs.
	 * @param excludeJobs If not <code>null</code>, jobs whose name matches any of
	 * these matcher will be ignored. Use in case you don't care about limited set
	 * of jobs. These matchers will overrule <code>consideredJobs</code> results,
	 * job matched by both <code>consideredJobs</code> and <code>excludeJobs</code>
	 * will be excluded.
	 * @param skipSystemJobs If true then all system jobs are skipped.
	 */
	public JobIsRunning(Matcher[] consideredJobs, Matcher[] excludeJobs, boolean skipSystemJobs) {
		this.consideredJobs = consideredJobs;
		this.excludeJobs = excludeJobs;
		this.skipSystemJobs = skipSystemJobs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean test() {
		currentJobs = Job.getJobManager().find(null);
		for (Job job: currentJobs) {
			if (excludeJobs != null && CoreMatchers.anyOf(excludeJobs).matches(job.getName())) {
				log.debug("  job '%s' specified by excludeJobs matchers, skipped", job.getName());
				continue;
			}

			if (consideredJobs != null && !CoreMatchers.anyOf(consideredJobs).matches(job.getName())) {
				log.debug("  job '%s' is not listed in considered jobs, ignore it", job.getName());
				continue;
			}

			if (skipSystemJobs && job.isSystem()) { 
				log.debug("  job '%s' is a system job, skipped", job.getName());
				continue;
			}
			
			if (job.getState() == Job.SLEEPING) {
				log.debug("  job '%s' is not running, skipped", job.getName());
				continue;
			}

			/* there's no reason why this one should be ignored, lets wait... */
			log.debug("  job '%s' has no excuses, wait for it", job.getName());
			return true;
		}

		return false;
	}

	@Override
	public String description() {
		return "at least one job is running";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String errorMessage() {
		StringBuilder msg = new StringBuilder("The following jobs are still running\n");
		for (Job job: currentJobs) {
			if (excludeJobs != null && CoreMatchers.anyOf(excludeJobs).matches(job.getName()))
				continue;
			if (consideredJobs != null && !CoreMatchers.anyOf(consideredJobs).matches(job.getName()))
				continue;
			if (skipSystemJobs && job.isSystem()) 
				continue;
			if (job.getState() == Job.SLEEPING)
				continue;
			msg.append("\t").append(job.getName()).append("\n");
		}
		return msg.toString();
	}
}
