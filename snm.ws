| z1 z2 z3 used |
used := Set new.
1 to: 9 do: [:s | 
   used add: s.
   0 to: 9 do: [:e | 
      (used includes: e)  ifFalse: [
          used add: e.
          0 to: 9 do: [:n | 
             (used includes: n)  ifFalse: [
                used add: n.
                0 to: 9 do: [:d | 
                   (used includes: d) ifFalse: [
                      used add: d.
                      1 to: 1 do: [:m |
                         (used includes: m)  ifFalse: [
                            used add: m.
                            0 to: 9 do: [:o | 
                               (used includes: o) ifFalse: [
                                  used add: o.
                                  0 to: 9 do: [:r | 
                                     (used includes: r) ifFalse: [
                                        used add: r.
                                        0 to: 9 do: [:y | 
                                           (used includes: y) ifFalse: [ 
                                              z1 := 1000 * s + (100 * e) + (10 * n) + d.
                                              z2 := 1000 * m + (100 * o) + (10 * r) + e.
                                              z3 := 10000 * m + (1000 * o) + (100 * n) + (10 * e) + y.
                                              z1 + z2 = z3 ifTrue: [
					         Transcript show: '   ',z1 printString; cr; 
                                                            show: '+',z2 printString; cr; 
                                                            show: '---------';cr;
                                                            show: z3 printString; cr]]].
                                        used remove: r ]].
                                  used remove:o ]].
                            used remove: m ]]. 
                      used remove: d]].
                used remove: n]].
          used remove: e]].            
    used remove:s]. 