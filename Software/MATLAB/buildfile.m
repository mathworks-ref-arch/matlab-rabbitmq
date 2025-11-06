function plan = buildfile
    plan = buildplan;

    plan('test') = matlab.buildtool.tasks.TestTask( ...
        TestResults="results.xml");