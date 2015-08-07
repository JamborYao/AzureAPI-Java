package common;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.windowsazure.management.storage.models.*;
public class myConatainers {
	public static final String storageConnectionString = 
		    "DefaultEndpointsProtocol=http;" + 
		    "AccountName=danielfiletest;" + 
		    "AccountKey=NntZ4wYSLxyqxLJXTJUSCUscmZdO6BKN0KmZQU7pIUd31dba/k38T1jYimo+TuVy1EFrlJURWPFJ23RALur0Bw==";
	public static void main(String[] args) {
		int i=1;
		int d=i;
		// TODO Auto-generated method stub
			try{
					CloudStorageAccount account=CloudStorageAccount.parse(storageConnectionString);
					CloudBlobClient blobClient=account.createCloudBlobClient();
					blobClient.listContainers();
					
			}
			catch (Exception e)
			{
				
			}
	}

}
