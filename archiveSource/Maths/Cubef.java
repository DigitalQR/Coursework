package Tools.Maths;


public class Cubef{
	
	public Vector3f StartLocation, EndLocation;
	
	
	public Cubef(Vector3f StartLocation, Vector3f EndLocation){
		this.StartLocation = StartLocation;
		this.EndLocation = EndLocation;
	}
	
	public Vector3f getSize(){
		return new Vector3f(
				EndLocation.x-StartLocation.x,
				EndLocation.y-StartLocation.y,
				EndLocation.z-StartLocation.z
				);
	}
	
	public float[] getVertices(){
		float[] temp = {
				StartLocation.x, StartLocation.y, StartLocation.z,//FTL 0
				EndLocation.x, StartLocation.y, StartLocation.z,//FTR 1
				StartLocation.x, EndLocation.y, StartLocation.z,//FBL 2 
				EndLocation.x, EndLocation.y, StartLocation.z,//FBR 3

				StartLocation.x, StartLocation.y, EndLocation.z,//BTL 4
				EndLocation.x, StartLocation.y, EndLocation.z,//BTR 5
				StartLocation.x, EndLocation.y, EndLocation.z,//BBL 6
				EndLocation.x, EndLocation.y, EndLocation.z,//BBR 7
				
		};
		return temp;
	}
	
	public int[] getIndices(){
		int[] temp = {
				0,2,3, 0,3,1,
				4,6,2, 4,2,0,
				1,3,7, 1,7,5,
				4,0,1, 4,1,5,
				2,6,7, 2,7,3,
				6,4,5, 6,5,7
		};
		return temp;
	}
	
	public float[] getFrontVertices(){
		float[] temp = {
				StartLocation.x, StartLocation.y, StartLocation.z,//FTL 0
				EndLocation.x, StartLocation.y, StartLocation.z,//FTR 1
				StartLocation.x, EndLocation.y, StartLocation.z,//FBL 2 
				EndLocation.x, EndLocation.y, StartLocation.z,//FBR 3
		};
		return temp;
	}
	
	public int[] getFrontIndices(){
		int[] temp = {
				0,2,3, 0,3,1
		};
		return temp;
	}
}