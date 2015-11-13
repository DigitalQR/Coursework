using UnityEngine;
using System.Collections;

public class DisableAtStart : MonoBehaviour {
    
	void Start () {
        transform.gameObject.SetActive(false);
	}
	
}
