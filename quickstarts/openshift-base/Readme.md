# Clustering demo #
Demo application can be deployed to OpenShift or run locally following instructions for the method chosen by you.

### Notes: ###
  * For correct functionality you may need build current version of SilverWare from [GitHub.](https://github.com/SilverThings/SilverWare)_
  * First invocation perform actual lookup and is delayed by setting `silverware.cluster.lookup.timeout`
  * When killing nodes, it is possible that cluster state was not updated and remote method can be invoked on the node. [Fault tolerance system](https://github.com/SilverThings/SilverWare/wiki/Hystrix-Microservice-Provider) should prevent this.


### Functionality ###
  After deployment you can call HTTP GET on following URLs:
* __RoundRobinLookupStrategy demo:__ log message on remote node and also on local node. Observe that chosen node change in cyclic order.
  * `http://$ADDRESS$/silverware/rest/call/remoteRoundHello`
* __RandomRobinLookupStrategy demo:__ log message on remote node and also on local node. Observe that chosen node change randomly.
  * `http://$ADDRESS$/silverware/rest/call/remoteRandomHello`
* __Versioning demo:__ Return string containing queries and versions of injected beans.
  * `http://$ADDRESS$/silverware/rest/call/getVersion`
* __Custom object demo:__ Calls remote method with object parameter, which generates new custom class object and returns it.
  * `http://$ADDRESS$/silverware/rest/call/newObjectGeneration`

 ## Deployment to OpenShift ##
1. Download OpenShift local client from [here](https://github.com/openshift/origin/releases/tag/v1.3.1). 
2. Get access to OpenShift instance - more possibilities exist, but I recommend local start.
  * _Note: OpenShift Online is not an option because there are multiple bugs in Fabric8-Maven-Plugin which prevent correct deployment._
 * Run OpenShift locally by using `oc cluster up` command. 
 Follow [these instructions](https://github.com/openshift/origin/blob/release-1.4/docs/cluster_up_down.md) for setup.
   * Observe that you are logged in automatically.
   * You can access web console. URL should be included in the command output.
3. Create project  `oc new-project silverware` (You can choose the project name,
 but you need to use it in the following steps.) 
4. Add additional privileges for Jgroups node discovery in Kubernetes:
   * `oc policy add-role-to-user view system:serviceaccount:silverware:default -n silverware`
5. Change directory to `./openshift-base`
6. Run `mvn clean install -P openshift -Dkubernetes.project=silverware` 
7. After successful deployment get route:
   * `oc get route`
   * field `HOST/PORT` is **$ADDRESS$** needed for querying
8. You can use OpenShift web interface to check logs or tail log of pods in a console:
   * `oc get po`
   * use name of pod `oc logs po/POD_NAME` 
   * _Note: You can use switch `-f` for tailing of logs._


  

## Standalone run ##
1. Change directory to `./openshift-base`
2. Run `mvn clean install -P standalone`
3. Start gateway with increased timeout for lookup in a cluster:
   *  `java -jar ./openshift-gateway/target/openshift-gateway-2.1-SNAPSHOT.jar silverware.cluster.lookup.timeout=3000`
   *  **$ADDRESS$** is `localhost:8080`
4. Start multiple instances of workers. Every worker must have different port for exposing rest interface.
 Port can be configured using `silverware.http.port` property
   *  `java -jar ./openshift-cluster-worker/target/openshift-cluster-worker-2.1-SNAPSHOT.jar -Dsilverware.http.port=PORT_NUMBER`
5. You can kill workers or run more workers and observe cluster invocations.
