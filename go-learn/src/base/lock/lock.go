package lock

import (
	"log"
	"sync"
)

var mutex = sync.Mutex{}
var ch = make(chan bool, 1)

func UseMutex() {
	mutex.Lock()
	log.Printf("%+v\n", mutex)
	mutex.Unlock()
}
func UseChan(n int) {
	ch <- true
	log.Print(n)
	<-ch
}
