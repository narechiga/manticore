(* *)


(*FindInstance[ 

ForAll[ x1,
ForAll[ x2,
ForAll[ x3,
ForAll[ x4,
	Implies[
		x1^2 + x2^2 + x3^2 + x4^2 <= 0.1,
		p1+x1*p2+x2*p3+x3*p4+x4*p5+x1^2*p6+x1*x2*p7+x2^2*p8+x1*x3*p9+x2*x3*p10+x3^2*p11+x1*x4*p12+x2*x4*p13+x3*x4*p14+x4^2*p15 <= 0
	] && Implies [
		Abs[ x1 ]  >= Pi/2,
		p1+x1*p2+x2*p3+x3*p4+x4*p5+x1^2*p6+x1*x2*p7+x2^2*p8+x1*x3*p9+x2*x3*p10+x3^2*p11+x1*x4*p12+x2*x4*p13+x3*x4*p14+x4^2*p15 > 0
	]
]]]],

{p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15 }, Reals ];*)


(*Reduce[
	ForAll[ x1,
	ForAll[ x2,
	ForAll[ x3,
	ForAll[ x4,

		Implies[
			x1^2 + x2^2 + x3^2 + x4^2 <= 0.1,
			-21.50050177+0.00105623691763412*x1+0.000512059109482266*x2-0.000407838674291838*x3+0.000639147660081107*x4+62.3214795267718*x1^2-0.011432065385236*x1*x2+62.3333862476154*x2^2+0.00285666944688501*x1*x3-0.0319234166066167*x2*x3+62.3188515657413*x3^2+0.00373869313442299*x1*x4-0.00212425740660649*x2*x4-0.00110061218521196*x3*x4+62.3325057788793*x4^2 <= 0
		] 
	]]]]

	, {x1,x2,x3,x4}, Reals ]*)
		
		
Reduce[
	ForAll[ x1,
	ForAll[ x2,
	ForAll[ x3,
	ForAll[ x4,
		
	Implies [
		x1^2 + x2^2 + x3^2 + x4^2 <= Pi/4,
		-21.50050177+0.00105623691763412*x1+0.000512059109482266*x2-0.000407838674291838*x3+0.000639147660081107*x4+62.3214795267718*x1^2-0.011432065385236*x1*x2+62.3333862476154*x2^2+0.00285666944688501*x1*x3-0.0319234166066167*x2*x3+62.3188515657413*x3^2+0.00373869313442299*x1*x4-0.00212425740660649*x2*x4-0.00110061218521196*x3*x4+62.3325057788793*x4^2 > 0
		]

	]]]]

, {x1,x2,x3,x4}, Reals ]
		
