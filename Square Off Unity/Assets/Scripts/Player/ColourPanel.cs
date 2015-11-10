using UnityEngine;
using System.Collections;

public class ColourPanel : MonoBehaviour {
    
    private Material material;

	// Use this for initialization
	void Start () {
        material = new Material(Shader.Find("Unlit/Color"));

        foreach (Transform child in transform)
        {
            child.gameObject.GetComponent<Renderer>().material = material;
        }
        recolour();
    }
    
    //Recolour from parent
    public void recolour() {
        material.color = GetComponentInParent<PlayerController>().colour;
    }

    void FixedUpdate()
    {
        recolour();
    }
}
